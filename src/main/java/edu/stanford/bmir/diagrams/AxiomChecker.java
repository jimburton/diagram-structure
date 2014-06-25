package edu.stanford.bmir.diagrams;

import com.google.common.base.Optional;
import com.google.common.collect.Sets;
import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.util.OWLClassExpressionVisitorExAdapter;

import java.util.Set;

/**
 * @author Matthew Horridge, Stanford University, Bio-Medical Informatics Research Group, Date: 24/06/2014
 */
public class AxiomChecker {

    private static final ClassExpressionChecker checker = new ClassExpressionChecker();

//    public Set<SubClassOfType> check(OWLEquivalentClassesAxiom ax) {
//        Set<SubClassOfType> result = Sets.newHashSet();
//        for(OWLSubClassOfAxiom sca : ax.asOWLSubClassOfAxioms()) {
//            result.addAll(check(sca));
//        }
//        return result;
//    }
//
    public Set<SubClassOfType> check(OWLSubClassOfAxiom ax) {
        Set<SubClassOfType> result = Sets.newHashSet();
        for(OWLClassExpression sub : ax.getSubClass().asDisjunctSet()) {
            for(OWLClassExpression sup : ax.getSuperClass().asConjunctSet()) {
                check(result, sub, sup);
            }
        }
        return result;
    }

    private void check(Set<SubClassOfType> result, OWLClassExpression sub, OWLClassExpression sup) {
        Optional<Type> subClassType = sub.accept(checker);
        Optional<Type> superClassType = sup.accept(checker);
        result.add(new SubClassOfType(subClassType, superClassType));
    }


    public static class ClassExpressionChecker extends OWLClassExpressionVisitorExAdapter<Optional<Type>> {
        @Override
        protected Optional<Type> handleDefault(OWLClassExpression ce) {
            return Optional.absent();
        }

        @Override
        public Optional<Type> visit(OWLClass ce) {
            return Optional.of(Type.CLASS_NAME);
        }

        @Override
        public Optional<Type> visit(OWLObjectIntersectionOf ce) {
            for(OWLClassExpression op : ce.getOperands()) {
                if(op.isAnonymous()) {
                    return Optional.absent();
                }
            }
            return Optional.of(Type.CONJUNCTION_OF_CLASS_NAMES);
        }

        @Override
        public Optional<Type> visit(OWLObjectUnionOf ce) {
            for(OWLClassExpression op : ce.getOperands()) {
                if(op.isAnonymous()) {
                    return Optional.absent();
                }
            }
            return Optional.of(Type.DISJUNCTION_OF_CLASS_NAMES);
        }

        @Override
        public Optional<Type> visit(OWLObjectComplementOf ce) {
            if(ce.isAnonymous()) {
                return Optional.absent();
            }
            else {
                return Optional.of(Type.NEGATION_OF_CLASS_NAME);
            }
        }

        @Override
        public Optional<Type> visit(OWLObjectSomeValuesFrom ce) {
            if(ce.getFiller().isAnonymous()) {
                return Optional.absent();
            }
            else {
                return Optional.of(Type.EXISTS_R_CLASS_NAME);
            }
        }

        @Override
        public Optional<Type> visit(OWLObjectAllValuesFrom ce) {
            if(ce.getFiller().isAnonymous()) {
                return Optional.absent();
            }
            else {
                return Optional.of(Type.FORALL_R_CLASS_NAME);
            }
        }
    }

}
