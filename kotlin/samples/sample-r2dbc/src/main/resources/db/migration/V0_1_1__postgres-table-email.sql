CREATE TABLE IF NOT EXISTS email_json
(
    id              UUID            NOT NULL,
    data            JSONB           NOT NULL,
    creation_time   TIMESTAMPTZ     NOT NULL    DEFAULT now(),
    update_time     TIMESTAMPTZ     NOT NULL    DEFAULT now()
);

CREATE INDEX IF NOT EXISTS email_pk                 ON email_json (id, creation_time);
CREATE INDEX IF NOT EXISTS idx_email_created_at     ON email_json USING btree (creation_time DESC);
CREATE INDEX IF NOT EXISTS idx_email_receiver       ON email_json USING btree ((data ->> 'receiver'::text));
CREATE INDEX IF NOT EXISTS idx_email_subject        ON email_json USING btree ((data ->> 'subject'::text));
CREATE INDEX IF NOT EXISTS idx_email_content        ON email_json USING btree ((data ->> 'content'::text));
CREATE INDEX IF NOT EXISTS idx_email_status         ON email_json USING btree ((data ->> 'status'::text));

COMMENT ON TABLE email_json IS 'Email history';

