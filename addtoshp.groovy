import geoscript.workspace.*
import geoscript.layer.*
import geoscript.feature.*
import geoscript.filter.Filter

// Read the WNV data file
File csvFile = new File("data/data.csv")
List lines = csvFile.readLines()

// The first line is the name of the columns
String firstLine = lines[0]
List fieldNames = firstLine.split(",") as List

// Use the columns to create new Fields, but skip the first column
List newFields = fieldNames.subList(1, fieldNames.size()).collect { String fieldName ->
	// Purely numeric field names cause problems so prefix with 'y'
	new Field("y" + fieldName, "double")
}

// Get the data directory as a Directory Workspace
Workspace wk = new Directory("data")
Layer inLayer = wk.get("ne_110m_admin_1_states_provinces_lakes")
Schema schema = inLayer.schema
// Add the year columns and create a new Layer
Schema newSchema = schema.addFields(newFields, "states")
Layer outLayer = wk.create(newSchema)

// Add all Features to the new Layer
outLayer.withWriter { Writer w ->
	inLayer.eachFeature { Feature f ->
    	w.add(f)
	}
}

// For each line (skipping the first) add values to the new shapefile
lines.subList(1, lines.size()).each { String line ->
	List items = line.split(",")
	String name = items[0]
	if (name.equals("Dist. of")) {
    	name = "District of Columbia"
	}
	Filter filter = new Filter("name='${name}'")
	items.eachWithIndex{ String item, int c ->
    	String fieldName = fieldNames[c]
    	if (!fieldName.equals("State")) {
        	Field field = outLayer.schema.field("y" + fieldName)
        	outLayer.update(field, Double.parseDouble(item), filter)
    	}
	}
}
