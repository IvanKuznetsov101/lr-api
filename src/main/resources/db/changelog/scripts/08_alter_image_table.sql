ALTER TABLE image
DROP CONSTRAINT image_id_event_fkey,
ADD CONSTRAINT image_id_event_fkey
FOREIGN KEY (id_event)
REFERENCES event(id_event)
ON DELETE CASCADE;