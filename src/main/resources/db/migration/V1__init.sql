-- 更新日時カラムの自動更新TRIGGER
CREATE FUNCTION set_updated_date() RETURNS TRIGGER AS $$
BEGIN
    IF (TG_OP = 'UPDATE') THEN
        NEW.update_date := CURRENT_TIMESTAMP;
        return NEW;
    END IF;
END;
$$ LANGUAGE plpgsql;

-- 更新回数カラムの自動更新TRIGGER
CREATE FUNCTION set_updated_count() RETURNS TRIGGER AS $$
BEGIN
    IF (TG_OP = 'UPDATE') THEN
        NEW.update_count := NEW.update_count + 1;
        return NEW;
    END IF;
END;
$$ LANGUAGE plpgsql;

-- ブックマスタ作成
CREATE TABLE m_book
(
    book_id serial NOT NULL PRIMARY KEY,
    name text NOT NULL,
    author text NOT NULL,
    create_date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    update_date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    update_count int NOT NULL DEFAULT 0
);

-- INDEX作成
CREATE INDEX ON m_book (name, author);

-- TRIGGER作成
CREATE TRIGGER updated_date BEFORE UPDATE ON m_book FOR EACH ROW EXECUTE PROCEDURE set_updated_date();
CREATE TRIGGER updated_count BEFORE UPDATE ON m_book FOR EACH ROW EXECUTE PROCEDURE set_updated_count();

-- 論理名設定
comment ON COLUMN m_book.book_id IS '書籍ID';
COMMENT ON COLUMN m_book.name IS '書籍名';
COMMENT ON COLUMN m_book.author IS '著者名';
COMMENT ON COLUMN m_book.create_date IS '作成日';
COMMENT ON COLUMN m_book.update_date IS '更新日';
COMMENT ON COLUMN m_book.update_count IS '更新回数';