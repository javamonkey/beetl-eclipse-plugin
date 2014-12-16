package org.beetl.editors;

import org.eclipse.swt.graphics.RGB;

public interface SyntaxColorConstants {
	RGB STATIC_TEXT = new RGB(0, 0, 0);
	RGB HOLDER = new RGB(0xff,0x00,0x00);
	RGB ST = new RGB(0xff,0x00,0x00);
	RGB ST_BACKGROUPD = new RGB(0x66,0xff,0x00);
	
	RGB NUMBER = new RGB(0, 0, 0xee);
	RGB STRING = new RGB(0x6a, 0x5A, 0xCD); // 6A5ACD
	RGB STATEMENT = new RGB(0x7f, 0x00, 0x55);//#7f0055
	RGB DEFAULT = new RGB(0, 0, 0);
	RGB COMMENT = new RGB(0x33, 0x99, 0x66); 
	RGB ERROR = new RGB(0xdc, 0xdc, 0xdc);
	

}
