package vook.server.api.helper;

import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.EntityPathBase;
import com.querydsl.core.types.dsl.PathBuilder;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class QuerydslHelper {
    public static <T extends Comparable<?>> List<OrderSpecifier<T>> toOrderSpecifiers(
            EntityPathBase<?> qClass,
            Pageable pageable,
            OrderSpecifier<T>... defaultOrderSpecifier
    ) {
        List<OrderSpecifier<T>> result = new ArrayList<>();

        if (pageable.getSort().isSorted()) {
            for (Sort.Order o : pageable.getSort()) {
                PathBuilder<?> pathBuilder = new PathBuilder<>(qClass.getType(), qClass.getMetadata());
                OrderSpecifier<T> order = new OrderSpecifier(
                        o.isAscending() ? Order.ASC : Order.DESC,
                        pathBuilder.get(o.getProperty())
                );
                result.add(order);
            }
        } else {
            Collections.addAll(result, defaultOrderSpecifier);
        }

        return result;
    }
}
