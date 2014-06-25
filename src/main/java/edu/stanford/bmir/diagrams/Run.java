package edu.stanford.bmir.diagrams;

import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;

import java.io.File;
import java.io.PrintWriter;

/**
 * @author Matthew Horridge, Stanford University, Bio-Medical Informatics Research Group, Date: 24/06/2014
 */
public class Run {

    public static void main(String[] args) {
        if(args.length == 0) {
            System.err.println("No ontology document IRI specified");
            System.exit(1);
        }
        IRI ontologyDocument = IRI.create(args[0]);
        try {
            OWLOntologyManager man = OWLManager.createOWLOntologyManager();
            OWLOntology ont = man.loadOntologyFromOntologyDocument(ontologyDocument);
            OntologyChecker checker = new OntologyChecker(ont);
            checker.check(new PrintWriter(System.out));
        } catch (OWLOntologyCreationException e) {
            System.err.println("Could not load ontology: " + e.getMessage());
        }

    }
}
