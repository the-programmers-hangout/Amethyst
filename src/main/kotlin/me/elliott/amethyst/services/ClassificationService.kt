package me.elliott.amethyst.services

import de.daslaboratorium.machinelearning.classifier.Classifier
import de.daslaboratorium.machinelearning.classifier.bayes.BayesClassifier
import me.elliott.amethyst.util.Constants
import java.io.File


var bayes: Classifier<String, String> = BayesClassifier<String, String>()

private fun getWords(code: String): List<String> = code.split(" ").toList()

fun init() {

//    val python = File("training/Python.training").readText()
//    train(python, "python")

    val ruby = File("training/Ruby.training").readText()
    train(ruby, "ruby")

//    val js = File("training/JavaScript.training").readText()
//    train(js, "js")

    print("Training complete.")
}

private fun train(code: String, language: String) {
    bayes.learn(language, getWords(code))
}



