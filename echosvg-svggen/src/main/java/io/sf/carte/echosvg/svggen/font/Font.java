/*

   See the NOTICE file distributed with this work for additional
   information regarding copyright ownership.

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.

 */
package io.sf.carte.echosvg.svggen.font;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

import io.sf.carte.echosvg.svggen.font.table.CmapTable;
import io.sf.carte.echosvg.svggen.font.table.GlyfTable;
import io.sf.carte.echosvg.svggen.font.table.HeadTable;
import io.sf.carte.echosvg.svggen.font.table.HheaTable;
import io.sf.carte.echosvg.svggen.font.table.HmtxTable;
import io.sf.carte.echosvg.svggen.font.table.LocaTable;
import io.sf.carte.echosvg.svggen.font.table.MaxpTable;
import io.sf.carte.echosvg.svggen.font.table.NameTable;
import io.sf.carte.echosvg.svggen.font.table.Os2Table;
import io.sf.carte.echosvg.svggen.font.table.PostTable;
import io.sf.carte.echosvg.svggen.font.table.Table;
import io.sf.carte.echosvg.svggen.font.table.TableDirectory;
import io.sf.carte.echosvg.svggen.font.table.TableFactory;

/**
 * The TrueType font.
 * 
 * @author For later modifications, see Git history.
 * @version $Id$
 * @author <a href="mailto:david@steadystate.co.uk">David Schweinsberg</a>
 */
public class Font {

	private String path;
//    private Interpreter interp = null;
//    private Parser parser = null;
	private TableDirectory tableDirectory = null;
	private Table[] tables;
	private Os2Table os2;
	private CmapTable cmap;
	private GlyfTable glyf;
	private HeadTable head;
	private HheaTable hhea;
	private HmtxTable hmtx;
	private LocaTable loca;
	private MaxpTable maxp;
	private NameTable name;
	private PostTable post;

	/**
	 * Constructor
	 */
	public Font() {
	}

	public Table getTable(int tableType) {
		for (Table table : tables) {
			if ((table != null) && (table.getType() == tableType)) {
				return table;
			}
		}
		return null;
	}

	public Os2Table getOS2Table() {
		return os2;
	}

	public CmapTable getCmapTable() {
		return cmap;
	}

	public HeadTable getHeadTable() {
		return head;
	}

	public HheaTable getHheaTable() {
		return hhea;
	}

	public HmtxTable getHmtxTable() {
		return hmtx;
	}

	public LocaTable getLocaTable() {
		return loca;
	}

	public MaxpTable getMaxpTable() {
		return maxp;
	}

	public NameTable getNameTable() {
		return name;
	}

	public PostTable getPostTable() {
		return post;
	}

	public int getAscent() {
		return hhea.getAscender();
	}

	public int getDescent() {
		return hhea.getDescender();
	}

	public int getNumGlyphs() {
		return maxp.getNumGlyphs();
	}

	public Glyph getGlyph(int i) {
		return (glyf.getDescription(i) != null)
				? new Glyph(glyf.getDescription(i), hmtx.getLeftSideBearing(i), hmtx.getAdvanceWidth(i))
				: null;
	}

	public String getPath() {
		return path;
	}

	public TableDirectory getTableDirectory() {
		return tableDirectory;
	}

	/**
	 * @param pathName Path to the TTF font file
	 */
	protected void read(String pathName) {
		path = pathName;
		File f = new File(pathName);

		if (!f.exists()) {
			// TODO: Throw TTException
			return;
		}

		try {
			RandomAccessFile raf = new RandomAccessFile(f, "r");
			tableDirectory = new TableDirectory(raf);
			tables = new Table[tableDirectory.getNumTables()];

			// Load each of the tables
			for (int i = 0; i < tableDirectory.getNumTables(); i++) {
				tables[i] = TableFactory.create(tableDirectory.getEntry(i), raf);
			}
			raf.close();

			// Get references to commonly used tables
			os2 = (Os2Table) getTable(Table.OS_2);
			cmap = (CmapTable) getTable(Table.cmap);
			glyf = (GlyfTable) getTable(Table.glyf);
			head = (HeadTable) getTable(Table.head);
			hhea = (HheaTable) getTable(Table.hhea);
			hmtx = (HmtxTable) getTable(Table.hmtx);
			loca = (LocaTable) getTable(Table.loca);
			maxp = (MaxpTable) getTable(Table.maxp);
			name = (NameTable) getTable(Table.name);
			post = (PostTable) getTable(Table.post);

			// Initialize the tables that require it
			hmtx.init(hhea.getNumberOfHMetrics(), maxp.getNumGlyphs() - hhea.getNumberOfHMetrics());
			loca.init(maxp.getNumGlyphs(), head.getIndexToLocFormat() == 0);
			glyf.init(maxp.getNumGlyphs(), loca);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static Font create() {
		return new Font();
	}

	/**
	 * @param pathName Path to the TTF font file
	 */
	public static Font create(String pathName) {
		Font f = new Font();
		f.read(pathName);
		return f;
	}
}
