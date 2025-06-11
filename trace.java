  
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.File;
import java.io.File;
public class trace{



static class SimpleCamera{
int width; // camera width
int height; // camera height
double fov; // camera fov
vec3 position;// camera position
vec3 lookdirection;// camera lookdirection
vec3 up;
vec3 lookat;// direction to which camera should look at


vec3 u, v, w;          // Camera basis vectors
vec3 horizontal;        // Viewport horizontal vector
vec3 vertical;          // Viewport vertical vector
vec3 lowerLeft;         // Lower-left corner of viewport
vec3 pixelDeltaU;       // Pixel horizontal step
vec3 pixelDeltaV; 
public SimpleCamera(int width, int height, vec3 position, vec3 lookat, vec3 up, double fov){

    this.width = width;
    this.height = height;
    this.lookat = lookat;
this.up = up;
this.fov = fov;
 

    
}
private void initiailize(){

  w = position.sub(lookat).normalize();   // Forward direction
            u = up.cross(w).normalize();            // Right direction
            v = w.cross(u);                         // Camera's Up direction
            
            // Calculate viewport dimensions
            double aspectRatio = (double)width / height;
            double theta = Math.toRadians(fov);
            double viewportHeight = 2.0 * Math.tan(theta / 2);
            double viewportWidth = viewportHeight * aspectRatio;
            
            // Calculate viewport vectors
            horizontal = u.mul(viewportWidth);
            vertical = v.mul(viewportHeight);
            
            // Calculate lower-left corner of viewport
            vec3 viewportCenter = position.sub(w);  // Viewport is 1 unit in front of camera
            lowerLeft = viewportCenter.sub(horizontal.mul(0.5))
                                      .sub(vertical.mul(0.5));
            
            // Calculate pixel step vectors
            pixelDeltaU = horizontal.mul(1.0 / width);
            pixelDeltaV = vertical.mul(1.0 / height);
    }
              

 public Ray getRay(double x, double y) {
            vec3 pixelCenter = lowerLeft
                .add(pixelDeltaU.mul(x))
                .add(pixelDeltaV.mul(height - y - 1));  // Flip Y for image coordinates
            
            return new Ray(position, pixelCenter.sub(position));}


static class vec3{

double x, y, z;

vec3(double x, double y, double z){


    this.x = x;
     this.y = y;
     this.z = z;
}
vec3 add(vec3 v) { 
    
    return new vec3(x+v.x, y+v.y, z+v.z); 

}
 vec3 sub(vec3 v) { 
            return new vec3(x-v.x, y-v.y, z-v.z); 
        
        }
        
        vec3 mul(double s) {
            
            return new vec3(x*s, y*s, z*s); 
        
        
        }
        double dot(vec3 v) {
             return x*v.x + y*v.y + z*v.z;
            
            
            }
        double length() { 
            
            return Math.sqrt(x*x + y*y + z*z); 
        
        
        }
        vec3 normalize() {
             return mul(1 / length()); 
            
            
            }
        vec3 negate(vec3 v){
            return new vec3(-x, -y, -z);
         }
 vec3 cross(vec3 v) {
            return new vec3(
                y * v.z - z * v.y,
                z * v.x - x * v.z,
                x * v.y - y * v.x
            );
        
        }
    
            }


static class Ray{
 vec3 origin;
 vec3 direction;

 public Ray(vec3 distance, vec3 origin ){
 this.origin = origin;
this.direction = direction.normalize();
 }

 }

static class Sphere{
vec3 center;
double radius;
vec3 color;

public Sphere(vec3 center, double radius, vec3 color){
this.center = center;
this.radius = radius;
this.color = color;


                }

  Double intersect(Ray ray) {
    //sphere to intersect movement of light
            vec3 oc = ray.origin.sub(center);
            double a = ray.direction.dot(ray.direction);
            double b = 2.0 * oc.dot(ray.direction);
            double c = oc.dot(oc) - radius*radius;
            double discriminant = b*b - 4*a*c;
            //if circle has no radius
            if (discriminant < 0) return null;

            double t = (-b - Math.sqrt(discriminant)) / (2*a);
            return t > 0.001 ? t : null;
        }
 }


public static void main(String args[])throws Exception{
 //camera
int width = 800;
 int height = 800;
 int fov = 120;
 double minDist = 4;

 Ray ray = new Ray(new vec3(0,0,1), new vec3(0,0,0));
  BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
 
  SimpleCamera camera = new SimpleCamera(width, height,new vec3(0,0,0), new vec3(0,1,0), new vec3(0,0,-1), fov);
  
//scene objects
Sphere sphere[]={new Sphere(new vec3(0, 0, -5), 1.0, new vec3(0.8, 0.2, 0.2)),  
            new Sphere(new vec3(2, 0, -5), 0.5, new vec3(0.2, 0.8, 0.2)),   
            new Sphere(new vec3(-2, 0, -5), 0.7,  new vec3(0.2, 0.2, 0.8))    

  };

  //light Scene
          vec3 light = new vec3(-5, 5, 0);
        //Render loop (if that makes sense)
          for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                // Generate ray through current pixel
                Ray Ray = camera.getRay(x, y);
                
                // Find closest intersection
                 minDist = Double.MAX_VALUE;
                vec3 color = new vec3(0, 0, 0); // Background color

}
          }
             for (Sphere spheres : sphere) {
                    Double t = spheres.intersect(ray);
                    if (t != null && t < minDist) {
                        minDist = t;
                        
                        // Calculate hit point
                        vec3 hitPoint = ray.origin.add(ray.direction.mul(t));
                        vec3 normal = hitPoint.sub(spheres.center).normalize();
                        
                        // Shadow check (with offset to prevent self-intersection)
                        vec3 toLight = light.sub(hitPoint).normalize();
                        vec3 shadowOrigin = hitPoint.add(normal.mul(0.001));
                        Ray shadowRay = new Ray(shadowOrigin, toLight);
                        boolean inShadow = false;
                        
                        for (Sphere s : sphere) {
                            Double st = s.intersect(shadowRay);
                            if (st != null) {
                                inShadow = true;
                                break;
                            }
                        }
                     }
    }
   

        // Save image
        ImageIO.write(image, "png", new File("render.png"));
        System.out.println("Rendering complete!");

}
}}