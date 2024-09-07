from enum import Enum

class RepoYamlDefinition:
    def __init__(self, repo_name, org_name, developers, additional_teams):
        self.repo_name = repo_name
        self.org_name = org_name
        self.team_name = f"{repo_name} Developers"
        self.developers = developers
        self.additional_teams = additional_teams

    def __init__(self):
        self.repo_name = None
        self.org_name = None
        self.team_name = None
        self.developers = []
        self.additional_teams = set()

    def get_repo_name(self):
        return self.repo_name

    def get_org_name(self):
        return self.org_name

    def get_team_name(self):
        return self.team_name

    def get_developers(self):
        return self.developers

    def set_developers(self, developers):
        self.developers = developers

    def get_additional_teams(self):
        return self.additional_teams

    def set_additional_teams(self, additional_teams):
        self.additional_teams = additional_teams


class SpecialYamlDefinition:
    DEFAULT_ORG_NAME = "jenkins-rpu-gsoc-2024"

    def __init__(self, team_name=None, developers=None):
        self.org_name = self.DEFAULT_ORG_NAME
        self.team_name = team_name
        self.developers = developers if developers is not None else []

    def get_org_name(self):
        return self.org_name

    def get_team_name(self):
        return self.team_name

    def get_developers(self):
        return self.developers

    def set_developers(self, developers):
        self.developers = developers

class AdditionalTeamDefinition:
    def __init__(self, team_name, role):
        self.team_name = team_name
        self.role = self.validate_role(role)

    def get_name(self):
        return self.team_name

    def set_name(self, team_name):
        self.team_name = team_name

    def get_role(self):
        return self.role

    def set_role(self, role):
        self.role = role

    def validate_role(self, role):
        if role is None:
            return None
        try:
            return Role(role.upper())
        except ValueError:
            raise ValueError(f"Invalid team role: {role}")

class DeveloperInfo:
    def __init__(self, ldap, github):
        self.ldap = ldap
        self.github = github

    def get_ldap_username(self):
        return self.ldap

    def set_ldap_username(self, ldap):
        self.ldap = ldap

    def get_github_username(self):
        return self.github

    def set_github_username(self, github):
        self.github = github

class Role(Enum):
    READ = 'READ'
    TRIAGE = 'TRIAGE'
    WRITE = 'WRITE'
    MAINTAIN = 'MAINTAIN'
    ADMIN = 'ADMIN'
