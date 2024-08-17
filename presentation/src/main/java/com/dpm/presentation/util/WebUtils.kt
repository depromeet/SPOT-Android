package com.dpm.presentation.util

fun getHTMLBody(svgString: String) = "<!DOCTYPE HTML>\n" +
        "<html>\n" +
        "\n" +
        "<head>\n" +
        "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1, maximum-scale=10\">\n" +
        "    <style>\n" +
        "        body {\n" +
        "            text-align: center;\n" +
        "        }\n" +
        "    </style>\n" +
        "</head>\n" +
        "\n" +
        "<body>\n" +
        "    <div id=\"div\" class=\"container\">\n" +
        "\t\n" +
        svgString +
        "\n" +
        "    <script src=\"stadium.js\"></script>\n" +
        "</body>\n" +
        "\n" +
        "</html>"