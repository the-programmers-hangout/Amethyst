/*
* NOT POSSIBLE WITHOUT A K-UTILS UPDATE
* CommandEvent needs to be made available to the conversation state container.
 */


//package me.elliott.amethyst.conversations
//
//import me.aberrantfox.kjdautils.api.dsl.*
//import me.aberrantfox.kjdautils.internal.command.CommandStruct
//import me.aberrantfox.kjdautils.internal.command.arguments.*
//import me.aberrantfox.kjdautils.internal.di.PersistenceService
//import me.elliott.amethyst.arguments.InstalledLanguageArg
//import me.elliott.amethyst.arguments.YesNoArg
//import me.elliott.amethyst.services.ScriptEngineService
//import me.elliott.amethyst.util.Utils
//import java.awt.Color
//
//
//@Convo
//fun addScriptConversation(persistenceService: PersistenceService) = conversation {
//
//    name = "add-script"
//    description = "Conversation that takes place with a user whenever a user wants to execute a script."
//
//    steps {
//        step {
//            prompt = embed {
//                title("Let's Add a Script.")
//                color(Color.CYAN)
//                description("I'm here to help you execute a script. Please follow the prompts.")
//
//                field {
//                    name = "Step 1"
//                    value = "Which language is the script you're providing written in? Supported languages are: " +
//                            "**${Utils.getInstalledLanguagesString()}**"
//                }
//            }
//            expect = InstalledLanguageArg
//        }
//        step {
//            prompt = embed {
//                title("Script Name")
//                color(Color.CYAN)
//                description("Now, I need the name of the script you're executing. No spaces are supported, " +
//                        "a valid example would be: `test-script`")
//            }
//            expect = WordArg
//            onComplete { language = it.responses.component1() as String }
//        }
//        step {
//            prompt = embed {
//                title("Should I watch the execution of this script?")
//                color(Color.magenta)
//                setDescription("I can watch the execution of this script if you like, and mark it as `stopped` " +
//                        "whenever it's finished executing. You might not want to have me do this for things like custom " +
//                        "`EventListener` objects or Commands, since it may effect their ability to function correctly.")
//            }
//            expect = YesNoArg
//        }
//        step {
//            prompt = embed {
//                title("Finally, please provide your script.")
//                color(Color.GREEN)
//                description("The script you intend to execute.")
//            }
//            expect = SentenceArg
//        }
//    }
//
//    onComplete {
//
//        ScriptEngineService().exec(
//                name = it.responses.component2() as String,
//                author = it.jda.getUserById(it.userId).asMention,
//                language = it.responses.component1() as String,
//                script = it.responses.component4() as String,
//                event = commandEvent,
//
//                )
//
//
//
//        return@onComplete
//    }
//}
//
//var language = ""


