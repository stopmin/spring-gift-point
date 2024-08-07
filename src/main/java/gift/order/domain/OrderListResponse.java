package gift.order.domain;

import java.util.List;

public class OrderListResponse {
    private List<OrderResponse> contents;
    private int number;
    private long totalElement;
    private int size;
    private boolean last;

    public OrderListResponse(List<OrderResponse> products, int number, long totalElement, int size, boolean last) {
        this.contents = products;
        this.number = number;
        this.totalElement = totalElement;
        this.size = size;
        this.last = last;
    }

    public List<OrderResponse> getContents() {
        return contents;
    }

    public int getNumber() {
        return number;
    }

    public long getTotalElement() {
        return totalElement;
    }

    public int getSize() {
        return size;
    }

    public boolean isLast() {
        return last;
    }
}
