# Amethyst - A Custom Script Management Bot

Amethyst is a bot designed to help you run various scripts and utilities at runtime in your preferred programming language.
This can make tasks like identifying a certain subset of members, listening for a specific message, or automatically banning raid bots when they join much easier. Currently, JavaScript, Python and Ruby are supported languages. 

## Running Scripts
Scripts are easily added using the `eval` command, the following parameters must be included:

- Script Name
- Language
- If Script Execution Should Be Tracked - Yes/No
- The Script

![Added Script](https://i.imgur.com/8DMcabY.png)

### Listing Previously Run Scripts
Once a script is successfully executed, you have the ability to manage their execution. A list of the previously run/running scripts can be accessed via the `list-scripts` command. You'll notice that each script has been issued a unique identifier, this is what you'll use to perform actions on an individual script.

![List Scripts](https://i.imgur.com/ZuEUfp2.png)
### Viewing a Script
Viewing the contents of a script is really straightforward, simply use the `view-script` command and supply the script's `id` as a parameter.

![View Script](https://i.imgur.com/zz3N4qs.png)

### Starting & Stopping a Script
You can easily start and stop scripts as well using the `start-script` and `stop-script` commands, each of these takes a script ID as a parameter.

------

## Built Using 

- [GraalVM](https://www.graalvm.org/) 
- [KUtils](https://gitlab.com/Aberrantfox/KUtils/)


## Deployment 


### Windows

1. Clone and cd into the root:  `cd /amethyst`
2. `%CD%/scripts/deploy.bat <YOUR_BOT_TOKEN> <ABSOLUTE_PATH_TO_CONFIG_FOLDER>`
    
Example run `%CD%/scripts/deploy.bat aokspdf.okwepofk.34p1o32kpo,pqo.sASDAwd /c/Users/account/amethyst`

## Linux

1. Clone and cd into the root `cd /amethyst`
2. `./scripts/deploy.sh <YOUR_BOT_TOKEN> <ABSOLUTE_PATH_TO_CONFIG_FOLDER>`

Example run `./scripts/deploy.sh aokspdf.okwepofk.34p1o32kpo,pqo.sASDAwd /home/me/config`

----
