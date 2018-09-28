package shaverma.logic;

public class WholesaleOrder extends Order {

    public WholesaleOrder(ForeignKey<BaseUser> manager, ForeignKey<BaseUser> provider, AccessorRegistry registry) {
        super(registry);
        this.setFrom(manager);
        this.setTo(provider);
    }

    public WholesaleOrder(ForeignKey<BaseUser> manager, ForeignKey<BaseUser> provider, AccessorRegistry registry, int id) {
        super(registry, id);
        this.setFrom(manager);
        this.setTo(provider);
    }

    @Override
    boolean canBeSubmitted() {
        return (getStatus() == OrderStatus.NEW);
    }

    @Override
    boolean canBeAccepted() {
        return (getStatus() == OrderStatus.SUBMITTED);
    }
}
