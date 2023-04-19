package com.omar.mentalcompanion.presentation.screens.introduction.utils

class IntroductionPageConstants {
    companion object {
        const val PAGES_COUNT = 8

        private const val P0_TEXT = "Welcome to \nMental Companion!"

        private const val P1_TEXT = "This app will help you to keep track of your mental health.\n\n" +
                "It will monitor your daily activities and detect any changes in your mood.\n\n"

        private const val P2_TEXT = "You will be asked to answer a few questions about your mood and activities.\n\n" +
                "The answers will help us better understand your mental health.\n\n"

        private const val P3_TEXT = "Remember that this app is in a very early stage of development.\n\n" +
                "And thank you very much for taking the time to try it out! 🙏\n\n"

        private const val P4_TEXT = "Let's get started!🚀 \n\n" +
                "The app will ask you to allow access for the following permissions:\n\n"

        private const val P5_TEXT = "Location:\n" +
                "Needed to get an idea about the places you usually visit."

        private const val P6_TEXT = "Applications Usage:\n" +
                "Needed to get an idea about how much time you spend on your phone."

        private const val P7_TEXT = "Phone Calls:\n" +
                "Needed to get an idea how many calls you receive and make.\n\n" +
                "Note: the app only stores the duration of the calls only, and no other information."

        fun getPageText(page: Int): String {
            return when (page) {
                0 -> P0_TEXT
                1 -> P1_TEXT
                2 -> P2_TEXT
                3 -> P3_TEXT
                4 -> P4_TEXT
                5 -> P5_TEXT
                6 -> P6_TEXT
                7 -> P7_TEXT
                else -> ""
            }
        }
    }
}