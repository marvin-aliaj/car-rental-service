package com.spring.jdbc.car.rental.model.filter;


import lombok.Getter;
import lombok.Setter;

import static com.spring.jdbc.car.rental.model.enums.CONSTANT_VARIABLES.DEFAULT_LIMIT;
import static com.spring.jdbc.car.rental.model.enums.CONSTANT_VARIABLES.ENTITY_MINUS_ONE;

@Setter
@Getter
abstract class Filter {
    protected long entityId = ENTITY_MINUS_ONE;
    protected int limit = DEFAULT_LIMIT;
    protected int offset = 0;

    public boolean hasEntityId() {
        return entityId != ENTITY_MINUS_ONE;
    }

    public Filter() {
    }

    public Filter(long entityId, int limit, int offset) {
        this.entityId = entityId;
        this.limit = limit;
        this.offset = offset;
    }
}
