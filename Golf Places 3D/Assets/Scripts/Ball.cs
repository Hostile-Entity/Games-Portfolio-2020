using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.SceneManagement;
using UnityEngine.UI;

public class Ball : MonoBehaviour
{
    public Rigidbody BallRigidBody;

    public Transform CameraTransform;

    public Text forseText;

    public Text strokeText;

    private float Forse = 0;
    private int Strokes = 0;

    // Start is called before the first frame update
    void Start()
    {
        
    }

    // Update is called once per frame
    void Update()
    {
        if (Input.GetKey(KeyCode.Space))
        {
            Forse += 400 * Time.deltaTime;
            forseText.text = "FORSE: " + (int)(Forse);
        }
        if (Input.GetKeyUp(KeyCode.Space))
        {
            Vector3 ballShotDirection = transform.position - CameraTransform.position;
            ballShotDirection.y = 0.0f;
            ballShotDirection.Normalize();

            BallRigidBody.AddForce(ballShotDirection * Forse);

            Forse = 0;
            ++Strokes;
            forseText.text = "FORSE: " + (int)(Forse);
            strokeText.text = "STROKE: " + Strokes;
            AudioSystem.Instance.playHit();
        }
    }

    private void OnCollisionEnter(Collision collision)
    {
        if (collision.gameObject.tag == "Pole")
        {
            AudioSystem.Instance.playWin();
            if (SceneManager.GetActiveScene().buildIndex != 3)
            {
                SceneManager.LoadScene(SceneManager.GetActiveScene().buildIndex + 1);
            }
            else
            {
                SceneManager.LoadScene(0);
            }
        } else
        {
            AudioSystem.Instance.playHitPole();
        }
    }
}
