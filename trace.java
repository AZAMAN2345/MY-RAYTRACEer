import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.File;
import java.io.File;
public class trace{

public class SimpleCamera{
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
SimpleCamera(int width, int height, vec3 position, vec3 lookat, vec3 up, double fov){

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
}

}
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
class Ray{
 vec3 origin;
 vec3 direction;

 Ray(vec3 distance, vec3 origin ){
 this.origin = origin;
this.direction = direction.normalize();
 }

 }

class Sphere{
vec3 center;
double radius;
vec3 color;
Sphere(vec3 center, int radius, vec3 color){
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
}

public static void main(String args[]){
 //camera
int width = 800;
 int height = 800;
 int fov = 120;
  BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
 
  SimpleCamera camera;
  camera = new SimpleCamera(width, height,new vec3(0,0,0), new vec3(0,1,0), new vec3(0,0,-1), fov);
  
//scene objects
  Sphere sphere[]={
 new Sphere(new Vec3(0, 0, -5), 1.0, new Vec3(0.8, 0.2, 0.2)),  
            new Sphere(new Vec3(2, 0, -5), 0.5, new Vec3(0.2, 0.8, 0.2)),   
            new Sphere(new Vec3(-2, 0, -5), 0.7, new Vec3(0.2, 0.2, 0.8))    

  };

  //light Scene
          Vec3 light = new Vec3(-5, 5, 0);
        //Render loop (if that makes sense)
          for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                // Generate ray through current pixel
                Ray ray = camera.getRay(x, y);
                
                // Find closest intersection
                double minDist = Double.MAX_VALUE;
                Vec3 color = new Vec3(0, 0, 0); // Background color

}
          }
             for (Sphere spheres : sphere) {
                    Double t = sphere.intersect(ray);
                    if (t != null && t < minDist) {
                        minDist = t;
                        
                        // Calculate hit point
                        Vec3 hitPoint = ray.origin.add(ray.direction.mul(t));
                        Vec3 normal = hitPoint.sub(sphere.center).normalize();
                        
                        // Shadow check (with offset to prevent self-intersection)
                        Vec3 toLight = light.sub(hitPoint).normalize();
                        Vec3 shadowOrigin = hitPoint.add(normal.mul(0.001));
                        Ray shadowRay = new Ray(shadowOrigin, toLight);
                        boolean inShadow = false;
                        
                        for (Sphere s : spheres) {
                            Double st = s.intersect(shadowRay);
                            if (st != null) {
                                inShadow = true;
                                break;
                            }
                        }
                     }
    }
    // Convert to RGB and set pixel
                int r = (int) (255 * Math.min(1, color.x));
                int g = (int) (255 * Math.min(1, color.y));
                int b = (int) (255 * Math.min(1, color.z));
                int rgb = (r << 16) | (g << 8) | b;
                image.setRGB(x, y, rgb);

        // Save image
        ImageIO.write(image, "png", new File("render.png"));
        System.out.println("Rendering complete!");

}


