
package br.gov.lexml.swing.util;

import java.util.regex.Pattern;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.DocumentFilter;
import javax.swing.text.SimpleAttributeSet;

public class PatternDocumentFilter extends DocumentFilter {

    private Pattern pattern;

    public PatternDocumentFilter(final Pattern pattern) {
        this.pattern = pattern;
    }

    @Override
    public void insertString(final FilterBypass fb, final int offset, final String string, final AttributeSet attr)
                                                                                                                   throws BadLocationException {
        replace(fb, offset, 0, string, attr);
    }

    @Override
    public void remove(final FilterBypass fb, final int offset, final int length) throws BadLocationException {
        replace(fb, offset, length, "", new SimpleAttributeSet());
    }

    @Override
    public void replace(final FilterBypass fb, final int offset, final int length, final String text,
                        final AttributeSet attrs) throws BadLocationException {

        Document doc = fb.getDocument();
        StringBuilder sb = new StringBuilder(doc.getText(0, doc.getLength()));
        sb.replace(offset, offset + length, text);

        String textoAlterado = sb.toString();
        if (textoAlterado.equals("") || pattern.matcher(textoAlterado).matches()) {
            super.replace(fb, 0, fb.getDocument().getLength(), textoAlterado, attrs);
        }
    }

}
