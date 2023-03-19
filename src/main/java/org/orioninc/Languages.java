package org.orioninc;

public enum Languages {
    JAVA("java", "Java"),
    JAVASCRIPT("javascript", "Javascript"),
    PYTHON("python", "Python"),
    C_SHARP("c%23", "C#"),
    PHP("php", "PHP"),
    HTML("html", "HTML"),
    C_PLUS_PLUS("c++", "C++"),
    CSS("css", "CSS"),
    SQL("sql", "SQL"),
    RUBY("ruby", "Ruby");

    private final String languageName;
    private final String languageRequest;

    Languages(String languageRequest, String languageName) {
        this.languageName = languageName;
        this.languageRequest = languageRequest;
    }

    public String getLanguageName() {
        return languageName;
    }

    public String getLanguageRequest() {
        return languageRequest;
    }
}
