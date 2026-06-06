package com.food.ordering.system.order.service.domain.ports.output.ai.order.noteinterpreter;

import com.food.ordering.system.domain.valueobject.OrderPreferences;

public interface OrderNoteInterpreter {

    OrderPreferences interpret(String orderNotes);
}
