const citySelect = document.querySelector('#city-select');
const reloadBtn = document.querySelector('#reload-btn');
const statusText = document.querySelector('#message');
const weatherBlock = document.querySelector('#weather');
const forecastBlock = document.querySelector('#forecast');
const sourcePill = document.querySelector('#source-pill');

function iconFor(code) {
  if (code === 0) return '☀️';
  if ([1, 2].includes(code)) return '🌤️';
  if (code === 3) return '☁️';
  if ([45, 48].includes(code)) return '🌫️';
  if ([71, 73, 75, 77, 85, 86].includes(code)) return '❄️';
  if ([95, 96, 99].includes(code)) return '⛈️';
  return '🌧️';
}

async function requestJSON(url) {
  const response = await fetch(url, { headers: { Accept: 'application/json' } });
  const data = await response.json();
  if (!response.ok) throw new Error(data.message || `请求失败（${response.status}）`);
  return data;
}

function displayWeather(data) {
  document.querySelector('#city-name').textContent = data.cityName;
  document.querySelector('#description').textContent = `${iconFor(data.weatherCode)} ${data.description}`;
  document.querySelector('#temperature').textContent = `${data.temperature}°`;
  document.querySelector('#humidity').textContent = `${data.humidity}%`;
  document.querySelector('#observed-at').textContent = new Date(data.observedAt).toLocaleString('zh-CN');
  sourcePill.textContent = data.source === 'demo' ? '演示数据 · 非实时' : 'Open-Meteo 实时';
  sourcePill.classList.toggle('live', data.source !== 'demo');
  forecastBlock.replaceChildren();
  for (const day of data.forecast) {
    const card = document.createElement('article');
    card.className = 'day';
    const date = document.createElement('div'); date.className = 'date'; date.textContent = day.date.slice(5);
    const icon = document.createElement('div'); icon.className = 'icon'; icon.textContent = iconFor(day.weatherCode);
    const desc = document.createElement('div'); desc.className = 'desc'; desc.textContent = day.description;
    const range = document.createElement('div'); range.className = 'range'; range.textContent = `${day.minTemperature}° / ${day.maxTemperature}°`;
    card.append(date, icon, desc, range);
    forecastBlock.append(card);
  }
  weatherBlock.hidden = false;
}

async function loadWeather() {
  const cityId = citySelect.value;
  if (!cityId) return;
  reloadBtn.disabled = true;
  statusText.textContent = '正在查询天气…';
  try {
    const data = await requestJSON(`/api/weather/${encodeURIComponent(cityId)}`);
    displayWeather(data);
    statusText.textContent = data.source === 'demo'
      ? '当前是可离线运行的演示模式；切换 WEATHER_MODE=live 可获取真实预报。'
      : '查询成功。数据来自 Open-Meteo，可能存在短暂缓存。';
  } catch (error) {
    statusText.textContent = `查询失败：${error.message}`;
    weatherBlock.hidden = true;
    forecastBlock.replaceChildren();
  } finally {
    reloadBtn.disabled = false;
  }
}

async function init() {
  try {
    const cities = await requestJSON('/api/cities');
    citySelect.replaceChildren();
    for (const city of cities) {
      const option = document.createElement('option');
      option.value = city.id; option.textContent = `${city.name} · ${city.province}`;
      citySelect.append(option);
    }
    citySelect.value = '101020100';
    await loadWeather();
  } catch (error) {
    statusText.textContent = `城市列表加载失败：${error.message}`;
  }
}

reloadBtn.addEventListener('click', loadWeather);
citySelect.addEventListener('change', loadWeather);
init();
