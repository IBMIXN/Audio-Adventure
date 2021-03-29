<img src="./resource/logo.jpg" width="60%"/>

In this day and age, technology advances faster than ever, but it creates a gap between the older and the younger generations. This results in social isolation between one another, which only worsens due to the recent pandemic and lockdowns. Our client Jon Mc Namara, who is an IBM employee, came up with this project to hopefully resolve this issue.

The purpose of the game is to solve social isolation (particularly for the elderly) causing acute problems relating to health, both mental and physical, it requires an engaging activity that brings joy while also exercising the mind. The game can be interacted via verbal commands to take our users on an engaging and enjoyable but also challenging experience.

The game is a storytelling game that revolves around a linear storyline with multiple choices that may lead to bad ends, and puzzles to be solved. There will be interactions with AI NPCs that react differently depending on the player's emotional response. For the player to advance through the game, he or she has to pick the correct response under different circumstances. The story's setting is situated in real parts of Bristol (local landmarks, areas of interest) and builds on local knowledge that long-time Bristolians would be aware of. This enables the player to explore the city without the need to go outdoors.

The game utilizes Watson Assistant (which incorporates Natural Language Understanding) and Tone Analyser (to determine emotional responses). It also uses Alexa skill (parses player's spoken words into strings), so that those at home can enjoy it without the need to download, configure and setup.

### Configuration
[Stack structure]()
##### Requirement
 - Amazon dev Account & IBM dev account 
 - AWS CLI & SAM 
 - Alexa Skill 
 - AWS Lambda, or other serverless computing services 
 - Watson Assistant
##### Configure AWS CLI & Docker
1. Setup *AWS CLI* on your local machine and download *SAM* for easier Lambda function management
. You can find tutorials [here](https://docs.aws.amazon.com/serverless-application-model/latest/developerguide/serverless-sam-cli-install.html). 
otherwise it is also possible to modify directly the config file located at `~/.aws/credentials
`. You
 can find
 your *AWS
 CLI* tokens & passcode
 in the AWS console.
2. (Optional) Install docker if you want to debug and test the function locally, you can find
 more info on this [link](https://docs.aws.amazon.com/serverless-application-model/latest/developerguide/serverless-test-and-debug.html). **note**: don't forget to start Docker Daemon.  
 E.g. `sudo dockerd`.
##### Build Alexa & Watson Assistant Model
Using models in `model` folder to build Alexa skill and Watson Assistant Skill. Just upload the
 appropriate json files and build models.
##### Configure Parameters
1. Copy/Paste the Alexa Skill ID to `template.yaml[SkillId]` section, so lambda function
 only
responds to events coming from the Alexa Skill.
2. Copy/Paste details related to Watson Assistant to the `config.xml` file (Clone it from the
 `config_template.xml` and assign the correct parameters).
##### Deployment
- Run `sam build` to build the function
- Run `sam deploy --guided` to upload the function and let CloudFormation to handle the stack
 deployment
- You can now test the project using the Alexa web testing interface. To fire up the skill, ask
 Alexa `Ask Watson Bard for a story`. 
 
##### Local Testing
Make sure you have docker installed and the daemon running on the background, e.g.`sudo dockerd`  
Run `sam local invoke -e events\help.json` to invoke the function with the given event. You can
 also use alternative event.
##### Clean up
Run `aws cloudformation delete-stack --stack-name sam-app --region region`
### Acknowledgements
+ [IBM Alexa Skill & Watson Assistant Sample](https://github.com/IBM/alexa-skill-watson-assistant)
