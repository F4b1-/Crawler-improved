/* -----------------------------------------------------------------------------
 - Project:   Remotely control the robot to dance
 - Copyright: Copyright (C) 2015 SunFounder. For details, check License folder.
 - Date:      2015/7/21
 * -----------------------------------------------------------------------------
 - Overview
  - This project was written for the Crawling robot desigened by Sunfounder.
    This version of the robot has 4 legs, and each leg is driven by 3 servos.
    This robot is driven by a Ardunio Nano Board with an expansion Board.
    We recommend that you view the product documentation before using. 
 - Request
  - This project requires some library files, which you can find in the head of
    this file. Make sure you have installed these files.
 - How to
  - Before use,you must to adjust the robot,in order to make it more accurate.
    - Adjustment operation
        1.uncomment ADJUST, make and run
        2.comment ADJUST, uncomment VERIFY
        3.measure real sites and set to real_site[4][3], make and run
        4.comment VERIFY, make and run
      The document describes in detail how to operate.
 * ---------------------------------------------------------------------------*/


const float y_default = x_default;
boolean keep_dancing = true;

/*
 - setup function
 * ---------------------------------------------------------------------------*/


/*
 - loop function
 * ---------------------------------------------------------------------------*/
void dance()
{
#ifdef INSTALL
  while (1);
#endif
#ifdef ADJUST
  while (1);
#endif
#ifdef VERIFY
  while (1);
#endif

//put your code here ---------------------------------------------------------

  Serial.println("Start a dance");

  move_body_absolute(0, 0, 0);
  delay(200);

#define move_length 40
#define move_up 40
/*
  move_body_absolute(0, move_length, 0);

  move_body_absolute(-move_length, 0, move_up);
  move_body_absolute(0, -move_length, 0);
  move_body_absolute(move_length, 0, move_up);
  move_body_absolute(0, move_length, 0);

  move_body_absolute(move_length, 0, move_up);
  move_body_absolute(0, -move_length, 0);
  move_body_absolute(-move_length, 0, move_up);
  move_body_absolute(0, move_length, 0);

  move_body_absolute(0, 0, 0);
*/
#define rotate_up 20
#define rotate_degree 20

  move_body_absolute(0, 0, rotate_up);
  delay(200);

  rotate_body_absolute_x(-rotate_degree, rotate_up);
  rotate_body_absolute_x(rotate_degree, rotate_up);
  move_body_absolute(0, 0, rotate_up);
  rotate_body_absolute_y(-rotate_degree, rotate_up);
  rotate_body_absolute_y(rotate_degree, rotate_up);
  move_body_absolute(0, 0, rotate_up);

  move_body_absolute(0, 0, 0);
  delay(200);

#define round_length 40
#define round_up 20
#define round_speed 5

  for (int degree = 0; degree < 360; degree += round_speed)
  {
    move_body_absolute(round_length*sin(degree*PI / 180), round_length*cos(degree*PI / 180), 0);
  }

  for (int degree = 360; degree >0; degree -= round_speed)
  {
    move_body_absolute(round_length*sin(degree*PI / 180), round_length*cos(degree*PI / 180), 0);
  }




  move_speed = stand_seat_speed;
  set_site(0, x_default - x_offset, y_start + y_step, z_default);
  set_site(1, x_default - x_offset, y_start + y_step, z_default);
  set_site(2, x_default + x_offset, y_start, z_default);
  set_site(3, x_default + x_offset, y_start, z_default);
//  move_body_absolute(0, 0, z_default - z_boot);
}

/*
 - rotate body by x axis function
 * ---------------------------------------------------------------------------*/
void rotate_body_absolute_x(float degree_x, float z)
{
  degree_x = degree_x * PI / 180;
  float dz = (length_side / 2 + y_default)*sin(degree_x);
  float dy = (length_side / 2 + y_default)*(1 - cos(degree_x));

  set_site(0, x_default, y_default - dy, z_default - z - dz);
  set_site(1, x_default, y_default - dy, z_default - z + dz);
  set_site(2, x_default, y_default - dy, z_default - z - dz);
  set_site(3, x_default, y_default - dy, z_default - z + dz);
  wait_all_reach();
}

/*
 - rotate body by y axis function
 * ---------------------------------------------------------------------------*/
void rotate_body_absolute_y(float degree_y, float z)
{
  degree_y = degree_y * PI / 180;
  float dz = (length_side / 2 + x_default)*sin(degree_y);
  float dx = (length_side / 2 + x_default)*(1 - cos(degree_y));

  set_site(0, x_default - dx, y_default, z_default - z + dz);
  set_site(1, x_default - dx, y_default, z_default - z + dz);
  set_site(2, x_default - dx, y_default, z_default - z - dz);
  set_site(3, x_default - dx, y_default, z_default - z - dz);
  wait_all_reach();
}

/*
 - move body function
 * ---------------------------------------------------------------------------*/
void move_body_relative(float dx, float dy, float dz)
{
  set_site(0, site_now[0][0] - dx, site_now[0][1] - dy, site_now[0][2] - dz);
  set_site(1, site_now[1][0] - dx, site_now[1][1] + dy, site_now[1][2] - dz);
  set_site(2, site_now[2][0] + dx, site_now[2][1] - dy, site_now[2][2] - dz);
  set_site(3, site_now[3][0] + dx, site_now[3][1] + dy, site_now[3][2] - dz);
  wait_all_reach();
}

/*
 - move body function
 * ---------------------------------------------------------------------------*/
void move_body_absolute(float x, float y, float z)
{
  set_site(0, x_default - x, y_default - y, z_default - z);
  set_site(1, x_default - x, y_default + y, z_default - z);
  set_site(2, x_default + x, y_default - y, z_default - z);
  set_site(3, x_default + x, y_default + y, z_default - z);
  wait_all_reach();
}


