package com.itssvkv.todolist.utils

import androidx.datastore.preferences.core.booleanPreferencesKey

object Constants {

    const val PICK_IMAGE = 1
    const val MODEL_INPUT_WIDTH = 224
    const val MODEL_INPUT_HEIGHT = 224
    const val NUM_CLASSES = 5

    const val APP_NAME = "ToDoList"
    const val TAG = "hhhhhhhhh"
    const val BASE_URL = ""
    val IS_FIRST_TIME = booleanPreferencesKey("IS_FIRST_TIME")
    val IS_LOGGED_IN = booleanPreferencesKey("IS_LOGGED_IN")



    var isFirstTime = true
    var isLoggedIn = false


    const val SWELLING = "https://www.healthdirect.gov.au/joint-pain-and-swelling"
    const val REDNESS = "https://www.mayoclinic.org/diseases-conditions/swollen-knee/symptoms-causes/syc-20378129"
    const val WEAKNESS = "https://prohealthclinic.co.uk/blog/weak-in-the-knees/"
    const val BANG_LOUDLY = "https://www.healthline.com/health/loud-pop-in-knee-followed-by-pain"
    const val INABILITY = "https://centenoschultz.com/symptom/cant-straighten-knee/"
//    const val INABILITY = "inability to fully strahten the knee"


    const val BICYCLE = "https://nofa1.itch.io/bicycle"
    const val SWIMMING = "https://nofa1.itch.io/swimming"
    const val STANDING = "https://nofa1.itch.io/malestand"
    const val STANDING_FROM_SET = "https://nofa1.itch.io/sit-to-stand"
    const val LAYING_MOANING = "https://nofa1.itch.io/laying-moaning"
    const val SITTING_POSE = "https://nofa1.itch.io/sitting-pose"
    const val DOUBLE_LEG = "https://nofa1.itch.io/double-leg"
    const val JUMPING_ROPE = "https://nofa1.itch.io/jumping-rope"
}
