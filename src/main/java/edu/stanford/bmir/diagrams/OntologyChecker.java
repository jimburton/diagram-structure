package edu.stanford.bmir.diagrams;

import com.google.common.base.Optional;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import org.semanticweb.owlapi.model.AxiomType;
import org.semanticweb.owlapi.model.OWLEquivalentClassesAxiom;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLSubClassOfAxiom;

import java.io.PrintWriter;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Matthew Horridge, Stanford University, Bio-Medical Informatics Research Group, Date: 24/06/2014
 */
public class OntologyChecker {

    private Map<SubClassOfType, AtomicInteger> subClassOfCountMap = Maps.newLinkedHashMap();

    private OWLOntology ont;

    public OntologyChecker(OWLOntology ont) {
        this.ont = ont;
        for(SubClassOfType type : getAllTypes()) {
            subClassOfCountMap.put(type, new AtomicInteger(0));
        }
    }

    private Set<SubClassOfType> getAllTypes() {
        Set<SubClassOfType> result = Sets.newHashSet();
        for(Optional<Type> subType : getVector()) {
            for(Optional<Type> superType : getVector()) {
                result.add(new SubClassOfType(subType, superType));
            }
        }
        return result;
    }

    private List<Optional<Type>> getVector() {
        List<Optional<Type>> result = Lists.newArrayList();
        result.add(Optional.<Type>absent());
        for(Type type : Type.values()) {
            result.add(Optional.<Type>of(type));
        }
        return result;
    }

    public void check(PrintWriter printWriter) {
        AxiomChecker checker = new AxiomChecker();
        for(OWLSubClassOfAxiom ax : ont.getAxioms(AxiomType.SUBCLASS_OF, true)) {
            process(checker, ax);
        }
        for(OWLEquivalentClassesAxiom ax : ont.getAxioms(AxiomType.EQUIVALENT_CLASSES, true)) {
            for(OWLSubClassOfAxiom sca : ax.asOWLSubClassOfAxioms()) {
                process(checker, sca);
            }
        }

        printWriter.print(" & ");
        for(Iterator<Optional<Type>> it = getVector().iterator(); it.hasNext(); ) {
            Optional<Type> header = it.next();
            if (header.isPresent()) {
                printWriter.print("$" +  header.get().getPrintName() + "$");
            }
            else {
                printWriter.print("Other");
            }
            if (it.hasNext()) {
                printWriter.print(" & ");
            }
        }
        printWriter.println(" \\\\");
        for(Optional<Type> row : getVector()) {
            if(row.isPresent()) {
                printWriter.print("$" + row.get().getPrintName() + "$");
            }
            else {
                printWriter.append("Other");
            }
            printWriter.print(" & ");
            for(Iterator<Optional<Type>> it = getVector().iterator(); it.hasNext(); ) {
                Optional<Type> col = it.next();
                int val = subClassOfCountMap.get(new SubClassOfType(row, col)).get();
                printWriter.print(val);
                if (it.hasNext()) {
                    printWriter.print(" & ");
                }
                else {
                    printWriter.print(" \\\\");
                }
            }
            printWriter.print("\n");
        }
        printWriter.flush();
    }

    private void process(AxiomChecker checker, OWLSubClassOfAxiom ax) {
        Set<SubClassOfType> types = checker.check(ax);
        for(SubClassOfType type : types) {
            subClassOfCountMap.get(type).getAndIncrement();
        }
    }
}
