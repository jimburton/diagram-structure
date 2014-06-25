package edu.stanford.bmir.diagrams;

import com.google.common.base.Optional;

/**
 * @author Matthew Horridge, Stanford University, Bio-Medical Informatics Research Group, Date: 24/06/2014
 */
public class SubClassOfType {

    private Optional<Type> subClassType;

    private Optional<Type> superClassType;

    public SubClassOfType(Optional<Type> subClassType, Optional<Type> superClassType) {
        this.subClassType = subClassType;
        this.superClassType = superClassType;
    }

    public Optional<Type> getSubClassType() {
        return subClassType;
    }

    public Optional<Type> getSuperClassType() {
        return superClassType;
    }

    @Override
    public int hashCode() {
        return "SubClassOfType".hashCode() + subClassType.hashCode() + superClassType.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if(o == this) {
            return true;
        }
        if(!(o instanceof SubClassOfType)) {
            return false;
        }
        SubClassOfType other = (SubClassOfType) o;
        return this.subClassType.equals(subClassType) && this.superClassType.equals(other.superClassType);
    }
}
