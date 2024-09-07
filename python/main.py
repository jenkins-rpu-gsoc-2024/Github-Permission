from queue import Full
import re
import github
from ruamel.yaml import YAML
from ruamel.yaml.scalarstring import DoubleQuotedScalarString
from pathlib import Path
from github import Github, GithubException
from ruamel.yaml import YAML, scalarstring
from pathlib import Path
import sys
import os
import logging

from yaml_definitions import DeveloperInfo, RepoYamlDefinition, SpecialYamlDefinition

logging.basicConfig(level=logging.DEBUG, format='%(message)s', stream=sys.stdout)

# Create an instance of the logger class
logger = logging.getLogger(__name__)

class SyncMain:
    def __init__(self, github_client):
        self.github_client = github_client

    def run(self, args):
        if len(args) == 0:
            raise ValueError("No file path provided.")
        
        for yaml_file_path in args:
            logger.info(f"Processing team for: {yaml_file_path}")
            team = FetchYamlData.load_team(yaml_file_path)
            merger = MergeByName(self.github_client)
            writer = WriteToYAML()
            if isinstance(team, RepoYamlDefinition):
                merger.merge_repo_team(team)
                writer.write_repo_team_to_yaml(team, yaml_file_path)
            elif isinstance(team, SpecialYamlDefinition):
                logger.info("This is a special team.")
                merger.merge_special_team(team)
                writer.write_special_team_to_yaml(team, yaml_file_path)

class FetchYamlData:
    PERMISSIONS_PATH = Path('permissions').resolve()
    TEAMS_PATH = Path('teams').resolve()

    def __init__(self, file_path):
        self.resolved_path = self.resolve_file_path(file_path)
        self.load_yaml_configuration(self.resolved_path)

    @staticmethod
    def load_team(file_path):
        resolved_path = FetchYamlData.resolve_file_path(file_path)
        team_config = FetchYamlData.load_yaml_configuration(resolved_path)

        if file_path.startswith("permissions/"):
            return FetchYamlData.parse_permissions_team_definition(team_config)
        elif file_path.startswith("teams/"):
            return FetchYamlData.parse_teams_team_definition(team_config)
        else:
            raise ValueError("Unsupported file path: " + file_path)

    @staticmethod
    def resolve_file_path(file_path):
        basePath = FetchYamlData.PERMISSIONS_PATH if file_path.startswith("permissions/") else FetchYamlData.TEAMS_PATH
        resolved_path = basePath.joinpath(file_path.split("/", 1)[1]).resolve()

        if not resolved_path.exists():
            raise FileNotFoundError(f"File does not exist: {resolved_path}")
        if not str(resolved_path).endswith('.yml'):
            raise ValueError("Invalid file type")
        if not resolved_path.is_relative_to(basePath):
            raise PermissionError("Attempted path traversal out of allowed directory")

        return resolved_path

    @staticmethod
    def load_yaml_configuration(path):
        yaml = YAML(typ='safe')
        try:
            with open(path, 'r') as file:
                return yaml.load(file)
        except Exception as e:
            logger.error(f"Failed to load YAML configuration: {path}")
            raise RuntimeError(f"Failed to load YAML configuration: {path}") from e

    @staticmethod
    def parse_permissions_team_definition(team_config):
        if "github" not in team_config or not isinstance(team_config["github"], str):
            logger.error("The 'github' field is missing or invalid")
            return None
        repo_path = team_config["github"].split('/')
        if len(repo_path) < 2:
            logger.error(f"Invalid GitHub path: {team_config['github']}")
            return None

        developers = FetchYamlData.extract_developers(team_config)
        return RepoYamlDefinition(repo_path[1], repo_path[0], developers, set())

    @staticmethod
    def parse_teams_team_definition(team_config):
        team_name = team_config.get("name", "")
        developers = FetchYamlData.extract_developers(team_config)

        if not team_name:
            if developers:
                raise ValueError("No valid team name found.")
            else:
                logger.error("No valid team name provided.")
                return None
        logger.info(f"Team name: {team_name}, developers: {developers}")
        return SpecialYamlDefinition(team_name, developers)

    @staticmethod
    def extract_developers(team_config):
        developers = []
        if "developers" in team_config and isinstance(team_config["developers"], list):
            for dev in team_config["developers"]:
                if isinstance(dev, str):
                    developers.append(DeveloperInfo(dev, None))
                else:
                    logger.error(f"Invalid developer entry: {dev}")
                    raise ValueError("Expected a list of developer usernames.")
        else:
            logger.error("Developer data is missing or incorrect")
            raise ValueError("Expected a list of developer usernames.")

        return developers
    
class MergeByName:
    def __init__(self, github_client):
        self.github_client = github_client

    def merge_repo_team(self, repo_team):
        logger.info(f"Merging repo team: {repo_team.team_name}")
        team_name = repo_team.team_name
        repo_name = repo_team.repo_name
        org_name = repo_team.org_name
        developers = repo_team.developers

        try:
            org = self.github_client.get_organization(org_name)
            repo = org.get_repo(repo_name)
            all_teams = repo.get_teams()
            matching_team = next((team for team in all_teams if team.name == team_name), None)

            if matching_team:
                members = matching_team.get_members()
                for member in members:
                    github_username = member.login
                    found = False
                    for developer in developers:
                        if developer.ldap_username == github_username:
                            logger.info(f"Merging Github username for: {github_username}")
                            developer.github_username = github_username
                            found = True
                            break

                    if not found:
                        logger.info(f"Adding new Github developer to list: {github_username}")
                        developers.append(DeveloperInfo(None, github_username))
            else:
                if developers:
                    logger.error(f"Team not found: {team_name}")

        except GithubException as e:
            logger.error(f"Failed to access GitHub API: {e}")
            raise

    def merge_special_team(self, special_team):
        logger.info(f"Merging special team: {special_team.team_name}")
        team_name = special_team.team_name
        org_name = special_team.org_name
        developers = special_team.developers

        try:
            org = self.github_client.get_organization(org_name)
            team = org.get_team_by_slug(to_slug(team_name))
        
            if team:
                members = team.get_members()
                for member in members:
                    github_username = member.login
                    found = False
                    for developer in developers:
                        if developer.ldap == github_username:
                            logger.info(f"Merging GitHub username for: {github_username}")
                            developer.github = github_username
                            found = True
                            break

                    if not found:
                        developers.append(DeveloperInfo(None, github_username))
                        logger.info(f"Adding new GitHub developer to list: {github_username}")

            else:
                if developers:
                    logger.error(f"Team not found: {team_name}")

        except GithubException as e:
            logger.error(f"Failed to access GitHub API: {e}")
            raise

class WriteToYAML:
    def __init__(self):
        self.yaml = YAML()
        self.yaml.preserve_quotes = True
        self.yaml.indent(mapping=4, sequence=4, offset=2)
        self.yaml.default_flow_style = False

    def write_repo_team_to_yaml(self, repo_team, file_path):
        path = Path(file_path)
        existing_data = self.read_existing_yaml(path)

        # update data
        developer_details = []
        for developer in repo_team.developers:
            dev_map = {
                "ldap": scalarstring.DoubleQuotedScalarString(developer.ldap_username),
                "github": scalarstring.DoubleQuotedScalarString(developer.github_username)
            }
            developer_details.append(dev_map)
        
        existing_data['developers'] = developer_details
        
        # write to yaml
        with path.open('w') as f:
            self.yaml.dump(existing_data, f)

    def write_special_team_to_yaml(self, special_team, file_path):
        yaml = self.yaml

        with open(file_path, 'r') as f:
            data = yaml.load(f)
        
        developer_details = []
        for developer in special_team.developers:
            dev_map = {
                "ldap": scalarstring.DoubleQuotedScalarString(developer.ldap if developer.ldap else ""),
                "github": scalarstring.DoubleQuotedScalarString(developer.github if developer.github else "")
            }
            developer_details.append(dev_map)
        
        data['developers'] = developer_details

        with open(file_path, 'w') as f:
            yaml.dump(data, f)

    

def to_slug(name):

    slug = name.lower()

    slug = re.sub(r'\s+', '-', slug)

    slug = re.sub(r'[^\w-]', '', slug)

    slug = re.sub(r'-+', '-', slug)

    slug = slug.strip('-')
    return slug

def main():
    args = ["teams/Main-Project.yml"]

    github_token = os.getenv("GITHUB_OAUTH")
    if not github_token:
        raise EnvironmentError("GitHub OAuth token is not set in the environment variables.")

    github_client = Github(github_token)

    sync_main = SyncMain(github_client)
    
    sync_main.run(args)


if __name__ == "__main__":
    main()