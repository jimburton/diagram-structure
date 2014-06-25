package edu.stanford.bmir.diagrams;

/**
 * @author Matthew Horridge, Stanford University, Bio-Medical Informatics Research Group, Date: 24/06/2014
 */
public enum Type {

    CLASS_NAME("A"),

    CONJUNCTION_OF_CLASS_NAMES("A \\sqcap B"),

    DISJUNCTION_OF_CLASS_NAMES("A \\sqcup B"),

    NEGATION_OF_CLASS_NAME("\\lnot A"),

    EXISTS_R_CLASS_NAME("\\exists R.A"),

    //EXISTS_R_CLASS_EXPRESSION,

    FORALL_R_CLASS_NAME("\\forall R.A"),

    //FORALL_R_CLASS_EXPRESSION,

//    COMPLEX_OTHER


    ;

    private String printName;

    Type(String printName) {
        this.printName = printName;
    }

    public String getPrintName() {
        return printName;
    }
}
