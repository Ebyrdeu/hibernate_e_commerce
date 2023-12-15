package entities;

import jakarta.persistence.Table;
import jakarta.persistence.*;
import org.hibernate.annotations.*;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "`order`", schema = "e_commerce")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id", nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "customer_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Customer customer;
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "order_to_product",
            joinColumns = {@JoinColumn(name = "order_id")},
            inverseJoinColumns = {@JoinColumn(name = "product_id")}
    )
    private Set<Product> products = new HashSet<>();
    @Column(name = "order_date")
    @CreationTimestamp(source = SourceType.DB)
    private Instant orderDate;
    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private Status status;
    @Column(name = "total", nullable = false, precision = 10, scale = 2)
    private BigDecimal total;
    @Column(name = "created_at")
    @CreationTimestamp(source = SourceType.DB)
    private Instant createdAt;
    @Column(name = "updated_at")
    @UpdateTimestamp(source = SourceType.DB)
    private Instant updatedAt;

    public Set<Product> products() {
        return products;
    }

    public Long id() {
        return id;
    }

    public Customer customer() {
        return customer;
    }

    public Instant orderDate() {
        return orderDate;
    }

    public Status status() {
        return status;
    }

    public BigDecimal total() {
        return total;
    }

    public Instant createdAt() {
        return createdAt;
    }

    public Instant updatedAt() {
        return updatedAt;
    }

    public Order setId(Long id) {
        this.id = id;
        return this;
    }

    public Order setCustomer(Customer customer) {
        this.customer = customer;
        return this;
    }

    public Order setOrderDate(Instant orderDate) {
        this.orderDate = orderDate;
        return this;
    }

    public Order setStatus(Status status) {
        this.status = status;
        return this;
    }

    public Order setTotal(BigDecimal total) {
        this.total = total;
        return this;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Order order = (Order) o;
        return Objects.equals(id, order.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
