from ruamel.yaml import YAML

input_file = 'teams/Main-Project.yml'
output_file = 'teams/modified_example.yaml'

yaml = YAML()
yaml.preserve_quotes = True 

with open(input_file, 'r') as f:
    data = yaml.load(f)


data['some_key'] = 'new_value'

with open(output_file, 'w') as f:
    yaml.dump(data, f)
