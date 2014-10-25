all: images/wnv.gif

data/data.dat:
	geoscript-groovy readpdf.groovy

data/data.csv: data/data.dat
	geoscript-groovy parsetext.groovy

data/ne_110m_admin_1_states_provinces_lakes.zip: data/data.csv
	geoscript-groovy downloadshp.groovy

data/states.shp: data/ne_110m_admin_1_states_provinces_lakes.zip
	geoscript-groovy addtoshp.groovy

images/wnv.gif: data/states.shp
	geoscript-groovy drawmaps.groovy

clean:
	rm -r data/
	rm -r images/
