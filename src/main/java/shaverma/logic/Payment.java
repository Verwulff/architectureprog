package shaverma.logic;

public class Payment extends Entity {

    private ForeignKey<BaseUser> from;
    private ForeignKey<BaseUser> to;
    private int amount;
    private PaymentStatus status;

    public enum PaymentStatus {
        OPEN,
        COMPLETE,
        CLOSED,
        CANCELED
    }

    Payment(ForeignKey<BaseUser> from, ForeignKey<BaseUser> to, int amount, AccessorRegistry registry) {
        super(registry);
        this.from = from;
        this.to = to;
        this.amount = amount;
        this.status = PaymentStatus.OPEN;
    }

    public Payment(ForeignKey<BaseUser> from, ForeignKey<BaseUser> to, int amount, PaymentStatus status, int id, AccessorRegistry registry) {
        super(registry);
        this.from = from;
        this.to = to;
        this.amount = amount;
        this.status = status;
        this.setId(id);
    }

    @Override
    protected Class accessorRegistryKey() {
        return Payment.class;
    }

    public void setPaid() {
        this.status = PaymentStatus.COMPLETE;
    }

    public void close() {
        this.status = PaymentStatus.CLOSED;
    }

    public void cancel() {
        this.status = PaymentStatus.CANCELED;
    }

    public PaymentStatus getStatus() {
        return status;
    }

    public BaseUser getSourceUser() {
        return (BaseUser) from.getEntity();
    }

    public BaseUser getTargetUser() {
        return (BaseUser) to.getEntity();
    }

    public int getAmount() {
        return amount;
    }
}
