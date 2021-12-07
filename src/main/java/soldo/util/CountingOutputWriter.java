/******************************************************************************
 * Copyright © 2013-2016 The Nxt Core Developers.                             *
 *                                                                            *
 * See the AUTHORS.txt, DEVELOPER-AGREEMENT.txt and LICENSE.txt files at      *
 * the top-level directory of this distribution for the individual copyright  *
 * holder information and the developer policies on copyright and licensing.  *
 *                                                                            *
 * Unless otherwise agreed in a custom licensing agreement, no part of the    *
 * Nxt software, including this file, may be copied, modified, propagated,    *
 * or distributed except according to the terms contained in the LICENSE.txt  *
 * file.                                                                      *
 *                                                                            *
 * Removal or modification of this copyright notice is prohibited.            *
 *                                                                            *
 ******************************************************************************/

package soldo.util;

import java.io.FilterWriter;
import java.io.IOException;
import java.io.Writer;

/**
 * CountingOutputWriter extends Writer to count the number of characters written
 */
public class CountingOutputWriter extends FilterWriter {

    /** Character count */
    private long count = 0;

    /**
     * Create the CountingOutputWriter for the specified writer
     *
     * @param   writer              Output writer
     */
    public CountingOutputWriter(Writer writer) {
        super(writer);
    }

    /**
     * Write a single character
     *
     * @param   c                   Character to be written
     * @throws  IOException         I/O error occurred
     */
    @Override
    public void write(int c) throws IOException {
        super.write(c);
        count++;
    }

    /**
     * Write an array of characters starting at the specified offset
     *
     * @param   cbuf                Characters to be written
     * @param   off                 Starting offset
     * @param   len                 Number of characters to write
     * @throws  IOException         I/O error occurred
     */
    @Override
    public void write(char[] cbuf, int off, int len) throws IOException {
        super.write(cbuf, off, len);
        count += len;
    }

    /**
     * Write a substring
     *
     * @param   s                   String to be written
     * @param   off                 Starting offset
     * @param   len                 Number of characters to write
     * @throws  IOException         I/O error occurred
     */
    @Override
    public void write(String s, int off, int len) throws IOException {
        super.write(s, off, len);
        count += len;
    }

    /**
     * Return the number of characters written
     *
     * @return                      Character count
     */
    public long getCount() {
        return count;
    }
}
