package code.frontend.libs.katlaf.lists;

import java.util.ArrayList;

public abstract class OrderableList<T extends Listable> extends SearchableList {
    public OrderableList(ArrayList<Listable> listables) {
        super(listables);
    }

    abstract DraggableListMember<T> newDraggableMember(Listable listable);

    @Override
    protected SimpleListMember newMember(Listable listable) {
        return newDraggableMember(listable);
    }
}
