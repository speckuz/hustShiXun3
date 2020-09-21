for ($i = 0; $i -lt 1; $i++) {
    echo "hello" > 1.txt
    git add .
    git commit -m "I am a robot"
    git push
    del 1.txt
    git add .
    git commit -m "I am a robot"
    git push
}