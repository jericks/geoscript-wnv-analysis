// Function for testing whether a string is a number
boolean isNumber(String str) {
	boolean isNum = false
	try {
    	Integer.parseInt(str.replaceAll(",",""))
    	isNum = true
	} catch(NumberFormatException e) {

	}
	return isNum
}

// Turn the PDF extracted text into a CSV file
File file = new File("data/data.dat")
Map<String, List> data = [:]
String currentName = null

// Trim off title and footer and then read in data
List lines = file.readLines().dropWhile{
	!it.startsWith("State")
}.takeWhile{
   !it.startsWith("Source:")
}.each { String line ->
	line = line.trim()
	if (line.length() > 0) {
    	List items = line.split(" ").findAll { it.trim().length() > 0 }
		// If the first item is not a number, then get the state name
		// and collect values for each year
    	if (!isNumber(items[0])) {
        	currentName = items.takeWhile { !isNumber(it) }.join(" ").trim()
        	int numWords =  currentName.split(" ").size()
        	data.put(currentName, items.subList(numWords, items.size()).collect{ it.replaceAll(",","")})
    	}
			// If the first item is a number, continue collecting values for the previous entry
			else {
        	data.get(currentName).addAll(items.collect{ it.replaceAll(",","")})
    	}
	}
}

// Turn data into a CSV file
File csvFile = new File("data/data.csv")
String NEW_LINE = System.getProperty("line.separator")
csvFile.withWriter { Writer w ->
	data.each { String key, List values ->
    	w.write("${key},${values.join(',')}")
    	w.write(NEW_LINE)
	}
}
