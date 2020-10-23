using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class Arrow : MonoBehaviour
{
    public Transform playerTransform;
    public Transform ballTransform;
    public Rigidbody ballRigidBody;

    private Vector3 ballShotDirection;
    private Quaternion ballRotation;
    private Vector3 startRotation;
    private float ballRadius;

    // Start is called before the first frame update
    void Start()
    {
        startRotation = transform.rotation.eulerAngles;

        ballRadius = ballTransform.localScale.x;
    }

    // Update is called once per frame
    void Update()
    {
        ballShotDirection = transform.position - playerTransform.position;
        ballShotDirection.y = 0.0f;
        ballShotDirection.Normalize();

        transform.position = ballTransform.position + ballShotDirection * (ballRadius + transform.localScale.x/2);

        transform.forward = ballShotDirection;
        ballRotation.eulerAngles = transform.rotation.eulerAngles + startRotation;
        transform.rotation = ballRotation;

        if (ballRigidBody.velocity.magnitude > 0.2)
        {
            gameObject.GetComponent<Renderer>().enabled = false;
        }
        else
        {
            gameObject.GetComponent<Renderer>().enabled = true;
        }
    }
}
