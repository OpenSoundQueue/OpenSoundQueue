package com.example.backend.util;

public class EmailTemplate {
    private final String head;
    private String body;

    public EmailTemplate() {
        head = """
                <meta charset="utf-8">
                    <style>
                      button {
                        border: 1px solid black;
                        color: black;
                        padding: 20px;
                      }
                    </style>""";

        body = "";
    }

    public String generateEmailTemplate() {
        String erg = "<!DOCTYPE html>\n" +
                "<html>\n" +
                "<head>\n" +
                head +
                "</head>\n" +
                "<body>\n" +
                body +
                "</body>\n" +
                "</html>\n";

        erg = erg.replaceAll("ä", "&auml;").replaceAll("Ä", "&Auml;").replaceAll("ö", "&ouml;").replaceAll("Ö", "&Ouml;").replaceAll("ü", "&uuml;").replaceAll("Ü", "&Uuml;");

        return erg;
    }

    public void addButton(String content, String link) {
        body += "<a href='"+link+"'><button>"+content+"</button></a>";
    }
    public void addTitle(String content) {
        body += "<h1>"+content+"</h1>";
    }
    public void addText(String content) {
        body += "<p>" + content + "</p>";
    }
    public void addLine() {
        body += "<hr>";
    }
}
