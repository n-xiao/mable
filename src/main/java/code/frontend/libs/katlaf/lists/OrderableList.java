/*
    Copyright (C) 2026 Nicholas Siow <nxiao.dev@gmail.com>
    DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU Affero General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Affero General Public License for more details.

    You should have received a copy of the GNU Affero General Public License
    along with this program.  If not, see <https://www.gnu.org/licenses/>.
*/

package code.frontend.libs.katlaf.lists;

import java.util.ArrayList;

public abstract class OrderableList<T extends Listable> extends SearchableList {
    public OrderableList(ArrayList<Listable> listables) {
        super(listables);
    }

    abstract DraggableListMember newDraggableMember(Listable listable);

    @Override
    protected SimpleListMember newMember(Listable listable) {
        return newDraggableMember(listable);
    }

    protected void reorderMember(final Listable listable, final int index) {
        this.getListables().remove(listable);
        this.getListables().add(index, listable);
        refreshOrder();
    }
}
