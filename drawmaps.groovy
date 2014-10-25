import javax.imageio.ImageIO
import java.awt.Graphics2D
import java.awt.Color
import geoscript.workspace.*
import geoscript.layer.*
import geoscript.feature.*
import geoscript.style.*
import geoscript.render.*

// Create all images in a directory
File dir = new File("images")
dir.mkdir()

// Get the states Layer
Workspace wk = new Directory("data")
Layer layer = wk.get("states")
Schema schema = layer.schema

// Read the WNV data file
File csvFile = new File("data/data.csv")
List columns = csvFile.readLines()[0].split(",")
List years = columns.subList(1, columns.size() - 1)

// Stamen Toner-lite
OSM osm = new OSM("Stamen Toner", [
        "http://a.tile.stamen.com/watercolor",
        "http://b.tile.stamen.com/watercolor",
        "http://c.tile.stamen.com/watercolor",
        "http://d.tile.stamen.com/watercolor"
])

// For each year create an image
List images = years.collect { String year ->
	println "Year: ${year}"

	// Style the Layer with a gradient based on the year
	String name = "y${year}"
	Field field = layer.schema.get(name)
	Gradient style = new Gradient(layer, field, "EqualInterval", 5, "Reds")
	layer.style = style

	// Render a gif
	Map map = new Map(
    	layers: [osm, layer],
    	bounds: layer.bounds.expandBy(0.3),
    	proj: layer.proj,
    	backgroundColor: "white",
    	type: "gif"
	)
	def image = map.renderToImage()

	// Draw the year as text on the rendered image
	def graphics = image.graphics
	graphics.paint = Color.BLACK
	graphics.font = new java.awt.Font("Arial", java.awt.Font.BOLD, 32)
	graphics.drawString(year,5,30)
	graphics.dispose()
	ImageIO.write(image, "gif", new File(dir, "${year}.gif"))
	image
}

// Create an animated GIF using all of the year images
GIF gif = new GIF()
gif.renderAnimated(images, new File(dir, "wnv.gif"), 1000)
