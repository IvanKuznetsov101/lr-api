ALTER TABLE light_room
DROP CONSTRAINT light_room_id_event_fkey,
ADD CONSTRAINT light_room_id_event_fkey
FOREIGN KEY (id_event)
REFERENCES event(id_event)
ON DELETE CASCADE;