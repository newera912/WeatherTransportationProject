
from noaaweather import weather

sfWeather = weather.noaa()
sfWeather.getByZip('94109')
print sfWeather.precipitation.liquid.tomorrow.max.value
print sfWeather.temperature.apparent.tomorrow.min.value
print sfWeather.temperature.apparent.value