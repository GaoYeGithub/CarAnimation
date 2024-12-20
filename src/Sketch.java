import processing.core.PApplet;

public class Sketch extends PApplet {
    private Car[] cars;
    private float roadY;

    public void settings() {
        size(1920, 1080);
    }

    public void setup() {
        roadY = height * 0.7f;
        cars = new Car[1];
        cars[0] = new Car(this, width/2, roadY, color(220, 50, 50), 0, "sports");
    }

    private void drawDynamicBackground() {
        for (int y = 0; y < height; y++) {
            float skyInter = map(y, 0, height, 0, 1);
            int skyColor = lerpColor(color(135, 206, 235), color(100, 180, 245), skyInter);
            stroke(skyColor);
            line(0, y, width, y);
        }

        noStroke();
        fill(100, 100, 100);
        rect(0, roadY, width, height - roadY);

        stroke(255, 255, 255, 150);
        strokeWeight(5);
        for (int x = 0; x < width; x += 40) {
            line(x, roadY + 25, x + 20, roadY + 25);
        }
    }

    public void draw() {
        drawDynamicBackground();
        for (Car car : cars) {
            car.move();
            car.draw();
        }
    }

    public void keyPressed() {
        if (key == CODED) {
            switch(keyCode) {
                case UP:    cars[0].accelerate(); break;
                case DOWN:  cars[0].brake(); break;
                case LEFT:  cars[0].turnLeft(); break;
                case RIGHT: cars[0].turnRight(); break;
            }
        }
    }

    public static void main(String[] args) {
        PApplet.main("Sketch");
    }
}

class Car {
    private PApplet p;
    private float x;
    private float y;
    private int carColor;
    private float speed;
    private float acceleration;
    private float maxSpeed;
    private float angle;
    private String carType;

    public Car(PApplet p, float x, float y, int carColor, float initialSpeed, String type) {
        this.p = p;
        this.x = x;
        this.y = y;
        this.carColor = carColor;
        this.speed = initialSpeed;
        this.carType = type;

        switch(type) {
            case "sports":
                this.maxSpeed = 10;
                this.acceleration = 0.2f;
                break;
            case "truck":
                this.maxSpeed = 5;
                this.acceleration = 0.1f;
                break;
            case "racing":
                this.maxSpeed = 12;
                this.acceleration = 0.3f;
                break;
            default:
                this.maxSpeed = 7;
                this.acceleration = 0.15f;
        }

        this.angle = 0;
    }

    public void move() {
        x += speed;
        y += p.sin(x * 0.05f) * 2;

        if (speed > 0 && x > p.width + 200) {
            x = -200;
            speed = p.random(2, maxSpeed) * (speed > 0 ? 1 : -1);
        } else if (speed < 0 && x < -200) {
            x = p.width + 200;
            speed = p.random(2, maxSpeed) * (speed > 0 ? 1 : -1);
        }
    }

    public void accelerate() {
        speed = p.min(speed + acceleration, maxSpeed);
    }

    public void brake() {
        speed = p.max(speed - acceleration, -maxSpeed);
    }

    public void turnLeft() {
        angle -= 0.1;
    }

    public void turnRight() {
        angle += 0.1;
    }

    public void draw() {
        p.pushMatrix();
        p.translate(x, y);
        p.rotate(angle);

        p.noStroke();
        p.fill(carColor);

        switch(carType) {
            case "sports":
                p.rect(0, 0, 100, 40, 15);
                break;
            case "truck":
                p.rect(0, 0, 120, 50, 10);
                break;
            case "racing":
                p.rect(0, 0, 90, 35, 20);
                break;
            default:
                p.rect(0, 0, 80, 40, 12);
        }

        p.fill(190, 230, 255, 200);
        p.rect(20, 5, 40, 15, 5);

        p.fill(50);
        p.ellipse(20, 35, 20, 20);
        p.ellipse(80, 35, 20, 20);

        p.popMatrix();
    }
}