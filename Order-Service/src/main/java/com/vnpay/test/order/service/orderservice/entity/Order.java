package com.vnpay.test.order.service.orderservice.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.eclipse.persistence.annotations.Partitioned;
import org.eclipse.persistence.annotations.RangePartition;
import org.eclipse.persistence.annotations.RangePartitioning;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "order_book")
@Setter
@Getter
@NoArgsConstructor
@RangePartitioning(
        name = "RangePartitioningByDate",
        partitionColumn = @Column(name = "order_date"),
        partitionValueType = Date.class,
        unionUnpartitionableQueries = true,
        partitions = {
                @RangePartition(connectionPool = "default", startValue = "2022-08-20", endValue = "2022-09-20"),
                @RangePartition(connectionPool = "node2", startValue = "2022-09-21", endValue = "2022-10-21"),
                @RangePartition(connectionPool = "node3", startValue = "2022-10-22")
        }
)
@Partitioned("RangePartitioningByDate")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(nullable = false, name = "order_date")
    @Temporal(TemporalType.DATE)
    private Date orderDate;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private StatusOrderEnum status;

    @NotNull
    @Column
    private long userId;

    @NotNull
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "order_books",
            joinColumns = @JoinColumn(name = "order_book_id"),
            inverseJoinColumns = @JoinColumn(name = "list_book_order_id"))
    private Set<OrderBook> books = new HashSet<>();

    @Column(columnDefinition = "number(19,2) default 0")
    private double total;
}
