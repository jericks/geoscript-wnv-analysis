// Download natural earth data
URL url = new URL("http://www.naturalearthdata.com/http//www.naturalearthdata.com/download/110m/cultural/ne_110m_admin_1_states_provinces_lakes.zip")
File file = new File("data/ne_110m_admin_1_states_provinces_lakes.zip")
if (!file.exists()) {
	url.withInputStream { inp ->
    	file.withOutputStream { out ->
        	out << inp
    	}
	}
}
// Unzip into the data directory
File dir = new File("data")
def ant = new AntBuilder()
ant.unzip(src: file, dest: dir)
