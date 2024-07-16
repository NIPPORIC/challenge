/*
 * This file is generated by jOOQ.
 */
package com.quo.challenge.generated.jooq;


import com.quo.challenge.generated.jooq.tables.MBook;
import org.jooq.Index;
import org.jooq.OrderField;
import org.jooq.impl.DSL;
import org.jooq.impl.Internal;


/**
 * A class modelling indexes of tables in public.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes", "this-escape" })
public class Indexes {

    // -------------------------------------------------------------------------
    // INDEX definitions
    // -------------------------------------------------------------------------

    public static final Index M_BOOK_NAME_AUTHOR_IDX = Internal.createIndex(DSL.name("m_book_name_author_idx"), MBook.M_BOOK, new OrderField[] { MBook.M_BOOK.NAME, MBook.M_BOOK.AUTHOR }, false);
}