BEGIN TRANSACTION;

INSERT INTO coffee(id, title) VALUES (DEFAULT, 'Capuchino');

INSERT INTO materials_warehouse(id, title, materials_left, max_amount) VALUES (DEFAULT, 'Water', 1000, 2000);

INSERT INTO materials_warehouse(id, title, materials_left, max_amount) VALUES (DEFAULT, 'Milk', 1000, 1000);

INSERT INTO materials_warehouse(id, title, materials_left, max_amount) VALUES (DEFAULT, 'Coffee beans', 90, 100);

INSERT INTO cofee_material(id, amount_needed, coffee_id, materials_warehouse_id) VALUES (
DEFAULT,
150,
SELECT id FROM coffee WHERE title='Capuchino',
SELECT id FROM materials_warehouse WHERE title = 'Water');

INSERT INTO cofee_material(id, amount_needed, coffee_id, materials_warehouse_id) VALUES (
DEFAULT,
100,
SELECT id FROM coffee WHERE title='Capuchino',
SELECT id FROM materials_warehouse WHERE title = 'Milk');

INSERT INTO cofee_material(id, amount_needed, coffee_id, materials_warehouse_id) VALUES (
DEFAULT,
15,
SELECT id FROM coffee WHERE title='Capuchino',
SELECT id FROM materials_warehouse WHERE title = 'Coffee beans');

COMMIT;
