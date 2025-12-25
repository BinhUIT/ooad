package com.example.ooad.service.payment.internal;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class PayOSCreateRequest {
    private final Long orderCode;
    private final Integer amount;
    private final String currency;
    private final String description;
    private final String returnUrl;
    private final String cancelUrl;
    private final List<Item> items;

    private PayOSCreateRequest(Builder builder) {
        this.orderCode = builder.orderCode;
        this.amount = builder.amount;
        this.currency = builder.currency;
        this.description = builder.description;
        this.returnUrl = builder.returnUrl;
        this.cancelUrl = builder.cancelUrl;
        this.items = Collections.unmodifiableList(new ArrayList<>(builder.items));
    }

    public Long getOrderCode() {
        return orderCode;
    }

    public Integer getAmount() {
        return amount;
    }

    public String getCurrency() {
        return currency;
    }

    public String getDescription() {
        return description;
    }

    public String getReturnUrl() {
        return returnUrl;
    }

    public String getCancelUrl() {
        return cancelUrl;
    }

    public List<Item> getItems() {
        return items;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private Long orderCode;
        private Integer amount;
        private String currency;
        private String description;
        private String returnUrl;
        private String cancelUrl;
        private List<Item> items = new ArrayList<>();

        public Builder orderCode(Long orderCode) {
            this.orderCode = orderCode;
            return this;
        }

        public Builder amount(Integer amount) {
            this.amount = amount;
            return this;
        }

        public Builder currency(String currency) {
            this.currency = currency;
            return this;
        }

        public Builder description(String description) {
            this.description = description;
            return this;
        }

        public Builder returnUrl(String returnUrl) {
            this.returnUrl = returnUrl;
            return this;
        }

        public Builder cancelUrl(String cancelUrl) {
            this.cancelUrl = cancelUrl;
            return this;
        }

        public Builder items(List<Item> items) {
            this.items = new ArrayList<>(Objects.requireNonNull(items, "items"));
            return this;
        }

        public Builder addItem(Item item) {
            this.items.add(Objects.requireNonNull(item, "item"));
            return this;
        }

        public PayOSCreateRequest build() {
            if (orderCode == null) {
                throw new IllegalStateException("orderCode is required");
            }
            if (amount == null) {
                throw new IllegalStateException("amount is required");
            }
            if (currency == null || currency.isBlank()) {
                throw new IllegalStateException("currency is required");
            }
            if (items.isEmpty()) {
                throw new IllegalStateException("at least one item is required");
            }
            return new PayOSCreateRequest(this);
        }
    }

    public static class Item {
        private final String name;
        private final Integer quantity;
        private final Integer price;

        private Item(Item.Builder builder) {
            this.name = builder.name;
            this.quantity = builder.quantity;
            this.price = builder.price;
        }

        public String getName() {
            return name;
        }

        public Integer getQuantity() {
            return quantity;
        }

        public Integer getPrice() {
            return price;
        }

        public static Builder builder() {
            return new Builder();
        }

        public static class Builder {
            private String name;
            private Integer quantity;
            private Integer price;

            public Builder name(String name) {
                this.name = name;
                return this;
            }

            public Builder quantity(Integer quantity) {
                this.quantity = quantity;
                return this;
            }

            public Builder price(Integer price) {
                this.price = price;
                return this;
            }

            public Item build() {
                if (name == null || name.isBlank()) {
                    throw new IllegalStateException("item name is required");
                }
                if (quantity == null || quantity <= 0) {
                    throw new IllegalStateException("item quantity must be greater than zero");
                }
                if (price == null || price < 0) {
                    throw new IllegalStateException("item price must be non-negative");
                }
                return new Item(this);
            }
        }
    }
}
