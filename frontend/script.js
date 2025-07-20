
let canvas = document.getElementById('omrCanvas');
let ctx = canvas.getContext('2d');
let img = new Image();
let coords = [];

document.getElementById('omrInput').onchange = function(e) {
  const file = e.target.files[0];
  const reader = new FileReader();
  reader.onload = function(event) {
    img.onload = function() {
      canvas.width = img.width;
      canvas.height = img.height;
      ctx.drawImage(img, 0, 0);
    }
    img.src = event.target.result;
  }
  reader.readAsDataURL(file);
};

canvas.addEventListener('click', function(event) {
  const rect = canvas.getBoundingClientRect();
  const x = event.clientX - rect.left;
  const y = event.clientY - rect.top;
  coords.push({ x, y });
  ctx.fillStyle = 'red';
  ctx.fillRect(x - 5, y - 5, 10, 10);
});

function submitOMR() {
  const input = document.getElementById('omrInput').files[0];
  if (!input) {
    alert("Please select an image first.");
    return;
  }

  const formData = new FormData();
  formData.append('image', input);
  formData.append('coords', JSON.stringify(coords));

  console.log("Submitting form data to backend...");

  fetch('http://localhost:8080/', {
    method: 'POST',
    body: formData
  })
  .then(res => res.json())
  .then(data => {
    console.log("Received response from backend:", data);
    document.getElementById('output').innerText = JSON.stringify(data, null, 2);
  })
  .catch(err => {
    console.error('Submission error:', err);
    document.getElementById('output').innerText = '❌ Submission failed. Check if Java server is running on port 8080.';
  });
}
