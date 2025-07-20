
import sys
import cv2
import pytesseract
import json

img = cv2.imread(sys.argv[1])
gray = cv2.cvtColor(img, cv2.COLOR_BGR2GRAY)
pytesseract.pytesseract.tesseract_cmd = r"C:\Program Files\Tesseract-OCR\tesseract.exe"
text = pytesseract.image_to_string(gray)

# Dummy score and answers (simulation)
result = {
    "name": "Sample Student",
    "roll_no": "12345678",
    "answers": {"1": "A", "2": "B", "3": "B", "4": "B", "5": "B"},
    "score": 5
}

print(json.dumps(result))
