insert into "order".orders(id, customer_id, restaurant_id, tracking_id, price, order_status, failure_messages)
values('d215b5f8-0249-4dc5-89a3-51fd148cfb17', 'd215b5f8-0249-4dc5-89a3-51fd148cfb41', 'd215b5f8-0249-4dc5-89a3-51fd148cfb45',
 'd215b5f8-0249-4dc5-89a3-51fd148cfb18', 100.00, 'PENDING', '');

insert into "order".order_items(id, order_id, product_id, price, quantity, sub_total)
values(1, 'd215b5f8-0249-4dc5-89a3-51fd148cfb17', 'd215b5f8-0249-4dc5-89a3-51fd148cfb47', 100.00, 1, 100.00);

insert into "order".order_address(id, order_id, street, postal_code, city)
values('d215b5f8-0249-4dc5-89a3-51fd148cfb15', 'd215b5f8-0249-4dc5-89a3-51fd148cfb17', 'test street', '1000AA', 'test city');

insert into "order".payment_outbox(id, saga_id, created_at, type, payload, outbox_status, saga_status, order_status, version)
values ('8904808e-286f-449b-9b56-b63ba8351cf2', '15a497c1-0f4b-4eff-b9f4-c402c8c07afa', current_timestamp, 'OrderProcessingSaga',
 '{"price": 100, "orderId": "ef471dac-ec22-43a7-a3f4-9d04195567a5", "createdAt": "2022-01-07T16:21:42.917756+01:00",
  "customerId": "d215b5f8-0249-4dc5-89a3-51fd148cfb41", "paymentOrderStatus": "PENDING"}',
'STARTED', 'STARTED', 'PENDING', 0);