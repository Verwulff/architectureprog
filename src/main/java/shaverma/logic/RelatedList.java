package shaverma.logic;

import java.util.List;

public class RelatedList<EntityName extends Entity> {
    private List<EntityName> listItems;
    private int sourceId;
    private Accessor sourceAccessor;
    private Accessor targetAccessor;

    public RelatedList(Entity source, Accessor sourceAccessor, Accessor targetAccessor) {
        sourceId = source.getId();
        this.sourceAccessor = sourceAccessor;
        this.targetAccessor = targetAccessor;
    }

    public RelatedList(List<EntityName> newListItems) {
        this.listItems = newListItems;
    }

    public List<EntityName> getListItems() {
        if (listItems == null) {
            listItems = (List<EntityName>) sourceAccessor.getRelatedList(sourceId, targetAccessor);
            return listItems;
        } else {
            return listItems;
        }
    }

    public void add(EntityName newListItem) {
        if (listItems == null) {
            getListItems();
        }
        listItems.add(newListItem);
    }
}
