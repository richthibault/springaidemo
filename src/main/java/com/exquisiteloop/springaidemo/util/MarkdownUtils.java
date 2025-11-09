package com.exquisiteloop.springaidemo.util;

import java.util.List;

import org.commonmark.Extension;
import org.commonmark.ext.gfm.tables.TablesExtension;
import org.commonmark.node.Node;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;

public class MarkdownUtils {

	public static String markdownToHtml(String text) {

		if(null==text)
			return "";

		// support for Github-favored markdown tables
		List<Extension> extensions = List.of(TablesExtension.create());

		Parser parser = Parser.builder()
				.extensions(extensions)
				.build();
		Node document = parser.parse(text);
		
		HtmlRenderer renderer = HtmlRenderer.builder()
				.extensions(extensions)
				.build(); 
		String newText = renderer.render(document);

		return newText;

	}

}
