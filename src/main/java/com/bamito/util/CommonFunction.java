package com.bamito.util;

import com.bamito.exception.CustomizedException;
import com.bamito.exception.ErrorCode;
import lombok.experimental.UtilityClass;
import org.springframework.data.domain.Sort;

import java.util.ArrayList;
import java.util.List;

@UtilityClass
public class CommonFunction {

    static public List<Sort.Order> createSortOrder(List<String> sort) {
        try {
            List<Sort.Order> orders = new ArrayList<>();
            // sort=["column1", "direction1", "column2", "direction2"]
            for(int i = 0; i < sort.size(); i+=2) {
                String column = sort.get(i);
                String direction = sort.get(i+1);
                orders.add(new Sort.Order(Sort.Direction.fromString(direction), column));
            }
            return orders;
        } catch (RuntimeException e) {
            throw new CustomizedException(ErrorCode.INVALID_PARAMETER);
        }
    }
}
